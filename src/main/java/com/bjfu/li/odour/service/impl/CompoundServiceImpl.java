package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.entity.Compound;
import com.bjfu.li.odour.mapper.CompoundMapper;
import com.bjfu.li.odour.service.ICompoundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
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

    @Value("${chemical-structure-dir}")
    String chemicalStructureDir;
    @Value("${chemical-structure-path}")
    String chemicalStructurePath;
    @Value("${mass-spectrogram-dir}")
    String massSpectrogramDir;
    @Value("${mass-spectrogram-path}")
    String massSpectrogramPath;

    @Override
    public List<Compound> searchCompounds(String property, String propertyDescription) {
        QueryWrapper<Compound> compoundQueryWrapper=new QueryWrapper<>();
        propertyDescription=propertyDescription.trim();
        switch (property) {
            case "compound_name":
                compoundQueryWrapper.like(property, propertyDescription)
                        .or()
                        .like("synonym", propertyDescription);
                break;
            case "odour_description":
                String[] descriptions = propertyDescription.split(",");
                for (String description : descriptions) {
                    description=description.trim();
                    compoundQueryWrapper.like(property, description);
                }
                break;
            case "cas_no":
                propertyDescription=propertyDescription.replace("-","");
                compoundQueryWrapper.eq(property, propertyDescription);
                break;
        }

        return compoundMapper.selectList(compoundQueryWrapper);
    }

    @Override
    public List<Compound> advancedSearch(Map<String, String> properties) {
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
        return compoundMapper.selectList(compoundQueryWrapper);
    }

    @Override
    public List<Compound> getNews() {
        return compoundMapper.selectNewsList();
    }

    @Override
    public boolean save(Compound compound) {
        String chemicalStructure=compound.getChemicalStructure();
        String massSpectrogram=compound.getMassSpectrogram();
        try {
            if(chemicalStructure!=null&&!chemicalStructure.equals("")) {
                chemicalStructure = chemicalStructurePath + Base64Utils.generateImage(chemicalStructure, chemicalStructureDir);
                compound.setChemicalStructure(chemicalStructure);
            }
            if(compound.getMassSpectrogram()!=null&&!massSpectrogram.equals("")) {
                massSpectrogram = massSpectrogramPath + Base64Utils.generateImage(massSpectrogram, massSpectrogramDir);
                compound.setMassSpectrogram(massSpectrogram);
            }

            if(compound.getOdourThreshold().intValue()==-1)
                compound.setOdourThreshold(null);
            compound.setCreateTime(LocalDateTime.now());
            compound.setUpdateTime(LocalDateTime.now());
            compound.setIsDeleted(0);
            compoundMapper.insert(compound);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeById(Integer id){
        Compound compound=compoundMapper.selectById(id);
        try {
            String chemicalStructureFilename=compound.getChemicalStructure().substring(compound.getChemicalStructure().lastIndexOf("/"));
            File oldChemicalStructure=new File(chemicalStructureDir+chemicalStructureFilename);
            if(oldChemicalStructure.exists())
                oldChemicalStructure.delete();
            String massSpectrogramFilename=compound.getMassSpectrogram().substring(compound.getMassSpectrogram().lastIndexOf("/"));
            File oldMassSpectrogram=new File(massSpectrogramDir+massSpectrogramFilename);
            if(oldMassSpectrogram.exists())
                oldMassSpectrogram.delete();

            compoundMapper.deleteById(id);
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
        Compound _compound=null;
        if(chemicalStructure.startsWith("data")||massSpectrogram.startsWith("data"))
            _compound=compoundMapper.selectById(compound.getId());

        try {
            if(!chemicalStructure.equals("")) {
                //不是base64就说明没有更新
                if(chemicalStructure.startsWith("data")&&_compound!=null){
                    String chemicalStructureFilename=_compound.getChemicalStructure().substring(_compound.getChemicalStructure().lastIndexOf("/"));
                    File oldChemicalStructure=new File(chemicalStructureDir+chemicalStructureFilename);
                    if(oldChemicalStructure.exists())
                        oldChemicalStructure.delete();

                    chemicalStructure = chemicalStructurePath + Base64Utils.generateImage(chemicalStructure, chemicalStructureDir);
                    compound.setChemicalStructure(chemicalStructure);
                }
            }
            //逻辑和上面一样
            if(!massSpectrogram.equals("")) {
                if(massSpectrogram.startsWith("data")&&_compound!=null) {
                    String massSpectrogramFilename=_compound.getMassSpectrogram().substring(_compound.getMassSpectrogram().lastIndexOf("/"));
                    File oldMassSpectrogram=new File(massSpectrogramDir+massSpectrogramFilename);
                    if(oldMassSpectrogram.exists())
                        oldMassSpectrogram.delete();

                    massSpectrogram = massSpectrogramPath + Base64Utils.generateImage(massSpectrogram, massSpectrogramDir);
                    compound.setMassSpectrogram(massSpectrogram);
                }
            }
            if(compound.getOdourThreshold().intValue()==-1)
                compound.setOdourThreshold(null);
            compound.setUpdateTime(LocalDateTime.now());
            compoundMapper.updateById(compound);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
