package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.PropertyMapper;
import tmall.entity.Property;
import tmall.entity.PropertyExample;
import tmall.service.PropertyService;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyServiceImpl(PropertyMapper propertyMapper) {
        this.propertyMapper = propertyMapper;
    }

    @Override
    public List<Property> selectPropertyList(Integer cid) {
        PropertyExample propertyExample = new PropertyExample();
        propertyExample.createCriteria().andCidEqualTo(cid);
        propertyExample.setOrderByClause("id DESC");

        return propertyMapper.selectByExample(propertyExample);
    }

    @Override
    public Property selectPropertyById(Integer id) {
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addProperty(Property property) {
        propertyMapper.insert(property);
    }

    @Override
    public void updatePropertyById(Property property) {
        propertyMapper.updateByPrimaryKey(property);
    }

    @Override
    public void deletePropertyById(Integer id) {
        propertyMapper.deleteByPrimaryKey(id);
    }
}
