package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.mapper.MRMapper;
import com.bjfu.li.odour.po.MR;
import com.bjfu.li.odour.mapper.RiMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.mapper.CompoundMapper;
import com.bjfu.li.odour.po.Ri;
import com.bjfu.li.odour.service.ICompoundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Service
public class CompoundServiceImpl extends ServiceImpl<CompoundMapper, Compound> implements ICompoundService {

    @Resource
    CompoundMapper compoundMapper;

    @Resource
    RiMapper riMapper;

    @Resource
    MRMapper mrMapper;

    @Value("${localImgPath}")
    String localImgPath;
    @Value("${networkImgPath}")
    String networkImgPath;

    @Override
    public List<Compound> searchCompounds(String property, String propertyDescription) {
        propertyDescription=propertyDescription.trim();
        switch (property) {
            case "compound_name":
                return compoundMapper.selectByCompoundName(propertyDescription);
            case "odour_description":
                String[] descriptions = propertyDescription.split(",");
                return   compoundMapper.selectByOdourDescription(descriptions);
            case "cas_no":
                propertyDescription=propertyDescription.replaceAll("-","");
                return compoundMapper.selectByCasNo(propertyDescription);
            case "compound_ri":
                int ri= Integer.parseInt(propertyDescription);
                return compoundMapper.selectByRi(ri-100,ri+100);
            case "measured":
                double measured= Double.parseDouble(propertyDescription);
                return compoundMapper.selectByMeasured(measured-0.05,measured+0.05);

        }

        return null;
    }

    @Override
    public List<Compound> advancedSearch(Map<String, String> properties) {
        if(properties.size()==0)
            return list();
        else if(properties.size()==1) {
            String property="",propertyDescription="";
            for(Map.Entry<String,String> e:properties.entrySet()){
                if(e!=null){
                    property=e.getKey();
                    propertyDescription=e.getValue();
                    break;
                }
            }
            return  searchCompounds(property,propertyDescription);
        }

        if(!properties.containsKey("odour_description"))
            return compoundMapper.selectByProperties(properties);
        else{
            QueryWrapper<Compound> compoundQueryWrapper=new QueryWrapper<>();
            for(Map.Entry<String,String> e:properties.entrySet()){
                String property=e.getKey();
                String propertyDescription=e.getValue().trim();
                switch (property) {
                    case "compound_name":
                        compoundQueryWrapper.and(wrapper->wrapper
                                .like(property, propertyDescription)
                                .or()
                                .like("synonym", propertyDescription));
                        break;
                    case "odour_description":
                        String[] descriptions = propertyDescription.split(",");
                        System.out.println(Arrays.toString(descriptions));
                        for (String description : descriptions) {
                            description=description.trim();
                            compoundQueryWrapper.like(property, description);
                        }
                        break;
                    case "cas_no":
                        compoundQueryWrapper.eq(property, propertyDescription);
                        break;
                }
            }
            List<Compound> compounds=compoundMapper.selectList(compoundQueryWrapper);
            for(Compound c:compounds){
                QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
                riQueryWrapper.eq("compound_id",c.getId());
                c.setRiList(riMapper.selectList(riQueryWrapper));

                QueryWrapper<MR> mrQueryWrapper=new QueryWrapper<>();
                mrQueryWrapper.eq("compound_id",c.getId());
                c.setMrList(mrMapper.selectList(mrQueryWrapper));;
            }
            return compounds;
        }

    }


    public Compound getById(Integer id){
        return compoundMapper.selectOne(id);
    }

    @Override
    public boolean save(Compound compound) {
        String chemicalStructure=compound.getChemicalStructure();
        String massSpectrogram=compound.getMassSpectrogram();
        String massSpectrogramNist=compound.getMassSpectrogramNist();
        try {
            if(chemicalStructure!=null&&!chemicalStructure.equals("")) {
                chemicalStructure = networkImgPath+"chemical structure/" + Base64Utils.generateImage(chemicalStructure, localImgPath+"chemical structure");
                compound.setChemicalStructure(chemicalStructure);
            }
            if(massSpectrogram!=null&&!massSpectrogram.equals("")) {
                massSpectrogram = networkImgPath+"Orbitrap-MS mass spectrometry/" + Base64Utils.generateImage(massSpectrogram, localImgPath+"Orbitrap-MS mass spectrometry");
                compound.setMassSpectrogram(massSpectrogram);
            }
            if(massSpectrogramNist!=null&&!massSpectrogramNist.equals("")) {
                massSpectrogramNist = networkImgPath+"Low-resolution mass spectrometry/" + Base64Utils.generateImage(massSpectrogramNist, localImgPath+"Low-resolution mass spectrometry");
                compound.setMassSpectrogramNist(massSpectrogramNist);
            }

            if(compound.getOdourThreshold().doubleValue()==-1||compound.getOdourThreshold().doubleValue()==0)
                compound.setOdourThreshold(null);

            compound.setCreateTime(LocalDateTime.now());
            compound.setUpdateTime(LocalDateTime.now());
            compound.setIsDeleted(0);
            compoundMapper.insert(compound);
            //必须放在插入之后再处理RI，因为Compound还没插入没有主键
            if(compound.getRiList().size()==0)
                riMapper.insert(new Ri(null,null,null,compound.getId()));
            else{
                int validNum=0;
                for(Ri ri:compound.getRiList()){
                    if(ri.getCompoundRi()==null||ri.getCompoundRi()==0)
                        continue;
                    validNum++;
                    ri.setCompoundId(compound.getId());
                    riMapper.insert(ri);
                }
                if(validNum==0)
                    riMapper.insert(new Ri(null,null,null,compound.getId()));
            }


            //离子碎片和相对丰度
            if(compound.getMrList().size()==0)
                mrMapper.insert(new MR(null,null,null,compound.getId()));
            else{
                int validNum=0;
                for(MR mr:compound.getMrList()){
                    if(mr.getMeasured().doubleValue()==0&&mr.getRelativeAbundance()==0)
                        continue;
                    if((mr.getMeasured()==null||mr.getMeasured().intValue()==0)||(mr.getRelativeAbundance()==null||mr.getRelativeAbundance()==0))
                        continue;
                    validNum++;
                    mr.setCompoundId(compound.getId());
                    mrMapper.insert(mr);
                }
                if(validNum==0)
                    mrMapper.insert(new MR(null,null,null,compound.getId()));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateById(Compound compound) {
        String chemicalStructure=compound.getChemicalStructure();
        String massSpectrogram=compound.getMassSpectrogram();
        String massSpectrogramNist=compound.getMassSpectrogramNist();
        Compound _compound=null;
        if(chemicalStructure.startsWith("data")||massSpectrogram.startsWith("data"))
            _compound=compoundMapper.selectById(compound.getId());

        try {
            if(!chemicalStructure.equals("")) {
                //不是base64就说明没有更新
                if(chemicalStructure.startsWith("data")&&_compound!=null){
                    if(_compound.getChemicalStructure().lastIndexOf("/")!=-1) {
                        String chemicalStructureFilename = _compound.getChemicalStructure().substring(_compound.getChemicalStructure().lastIndexOf("/"));
                        File oldChemicalStructure = new File(localImgPath + "chemical structure/" + chemicalStructureFilename);
                        if (oldChemicalStructure.exists())
                            oldChemicalStructure.delete();
                    }
                    chemicalStructure = networkImgPath+"chemical structure/" + Base64Utils.generateImage(chemicalStructure, localImgPath+"chemical structure/");
                    compound.setChemicalStructure(chemicalStructure);
                }
            }
            //逻辑和上面一样
            if(!massSpectrogram.equals("")) {
                if(massSpectrogram.startsWith("data")&&_compound!=null) {
                    if(_compound.getMassSpectrogram().lastIndexOf("/")!=-1) {
                        String massSpectrogramFilename = _compound.getMassSpectrogram().substring(_compound.getMassSpectrogram().lastIndexOf("/"));
                        File oldMassSpectrogram = new File(localImgPath + "Orbitrap-MS mass spectrometry/" + massSpectrogramFilename);
                        if (oldMassSpectrogram.exists())
                            oldMassSpectrogram.delete();
                    }
                    massSpectrogram = networkImgPath+"Orbitrap-MS mass spectrometry/" + Base64Utils.generateImage(massSpectrogram, localImgPath+"Orbitrap-MS mass spectrometry/");
                    compound.setMassSpectrogram(massSpectrogram);
                }
            }

            if(!massSpectrogramNist.equals("")) {
                if(massSpectrogramNist.startsWith("data")&&_compound!=null) {
                    if(_compound.getMassSpectrogramNist().lastIndexOf("/")!=-1) {
                        String massSpectrogramNistFilename = _compound.getMassSpectrogramNist().substring(_compound.getMassSpectrogramNist().lastIndexOf("/"));
                        File oldMassSpectrogramNist = new File(localImgPath + "Low-resolution mass spectrometry/" + massSpectrogramNistFilename);
                        if (oldMassSpectrogramNist.exists())
                            oldMassSpectrogramNist.delete();
                    }
                    massSpectrogramNist = networkImgPath+"Low-resolution mass spectrometry/" + Base64Utils.generateImage(massSpectrogramNist, localImgPath+"Low-resolution mass spectrometry/");
                    compound.setMassSpectrogramNist(massSpectrogramNist);
                }
            }
            if(compound.getOdourThreshold().intValue()==0)
                compound.setOdourThreshold(null);
            compound.setUpdateTime(LocalDateTime.now());
            compoundMapper.updateById(compound);

            QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
            riQueryWrapper.eq("compound_id",compound.getId());
            riMapper.delete(riQueryWrapper);

            if(compound.getRiList().size()==0)
                riMapper.insert(new Ri(null,null,null,compound.getId()));
            else{
                int validNum=0;
                for(Ri ri:compound.getRiList()){
                    if(ri.getCompoundRi()==null||ri.getCompoundRi()==0)
                        continue;
                    validNum++;
                    ri.setCompoundId(compound.getId());
                    riMapper.insert(ri);
                }
                if(validNum==0)
                    riMapper.insert(new Ri(null,null,null,compound.getId()));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeById(Integer id){
        Compound compound=compoundMapper.selectById(id);
        try {
            if(compound.getChemicalStructure().lastIndexOf("/")!=-1) {
                String chemicalStructureFilename = compound.getChemicalStructure().substring(compound.getChemicalStructure().lastIndexOf("/"));
                File oldChemicalStructure = new File(localImgPath+"chemical structure/" + chemicalStructureFilename);
                if (oldChemicalStructure.exists())
                    oldChemicalStructure.delete();
            }
            if(compound.getMassSpectrogram().lastIndexOf("/")!=-1) {
                String massSpectrogramFilename = compound.getMassSpectrogram().substring(compound.getMassSpectrogram().lastIndexOf("/"));
                File oldMassSpectrogram = new File(localImgPath+"Orbitrap-MS mass spectrometry/" + massSpectrogramFilename);
                if (oldMassSpectrogram.exists())
                    oldMassSpectrogram.delete();
            }

            if(compound.getMassSpectrogramNist().lastIndexOf("/")!=-1) {
                String massSpectrogramNistFilename = compound.getMassSpectrogramNist().substring(compound.getMassSpectrogram().lastIndexOf("/"));
                File oldMassSpectrogramNist = new File(localImgPath+"Low-resolution mass spectrometry/" + massSpectrogramNistFilename);
                if (oldMassSpectrogramNist.exists())
                    oldMassSpectrogramNist.delete();
            }

            QueryWrapper<MR> mrQueryWrapper=new QueryWrapper<>();
            mrQueryWrapper.eq("compound_id",id);
            mrMapper.delete(mrQueryWrapper);
            QueryWrapper<Ri> riQueryWrapper=new QueryWrapper<>();
            riQueryWrapper.eq("compound_id",id);
            riMapper.delete(riQueryWrapper);
            compoundMapper.deleteById(id);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public List<Compound> list(){
        return compoundMapper.selectAll();
    }

    @Override
    public List<Compound> getNews() {
        return compoundMapper.selectNewsList();
    }

}
