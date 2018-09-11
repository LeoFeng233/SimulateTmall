package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.PropertyMapper;
import tmall.dao.PropertyValueMapper;
import tmall.entity.Product;
import tmall.entity.Property;
import tmall.entity.PropertyValue;
import tmall.entity.PropertyValueExample;
import tmall.service.PropertyService;
import tmall.service.PropertyValueService;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    private final PropertyValueMapper propertyValueMapper;
    private final PropertyMapper propertyMapper;
    private final PropertyService propertyService;

    @Autowired
    public PropertyValueServiceImpl(PropertyValueMapper propertyValueMapper, PropertyMapper propertyMapper, PropertyService propertyService) {
        this.propertyValueMapper = propertyValueMapper;
        this.propertyMapper = propertyMapper;
        this.propertyService = propertyService;
    }

    @Override
    public void init(Product product) {

        List<Property> properties = propertyService.selectPropertyList(product.getCid());

        for (Property property : properties) {
            PropertyValue propertyValue = selectPropertyValue(property.getId(), product.getId());
            if (null == propertyValue) {
                propertyValue = new PropertyValue();
                propertyValue.setPid(property.getId());
                propertyValue.setPtid(propertyValue.getId());
                propertyValueMapper.insert(propertyValue);
            }
        }
    }

    @Override
    public List<PropertyValue> selectPropertyValueList(Integer pid) {
        PropertyValueExample propertyValueExample = new PropertyValueExample();
        propertyValueExample.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(propertyValueExample);

        for (PropertyValue propertyValue : propertyValues) {
            propertyValue.setProperty(propertyMapper.selectByPrimaryKey(pid));
        }

        return propertyValues;
    }

    @Override
    public PropertyValue selectPropertyValue(Integer pid, Integer ptid) {
        PropertyValueExample propertyValueExample = new PropertyValueExample();
        propertyValueExample.createCriteria().andPidEqualTo(pid).andPtidEqualTo(ptid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(propertyValueExample);
        if (propertyValues.isEmpty()) {
            return null;
        }

        return propertyValues.get(0);
    }


    @Override
    public void addPropertyValue(PropertyValue propertyValue) {
        propertyValueMapper.insert(propertyValue);
    }

    @Override
    public void updatePropertyValueById(PropertyValue propertyValue) {
        propertyValueMapper.updateByPrimaryKey(propertyValue);
    }

    @Override
    public void deletePropertyValueById(Integer id) {
        propertyValueMapper.deleteByPrimaryKey(id);
    }
}
