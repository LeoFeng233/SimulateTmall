package tmall.service;

import tmall.entity.Product;
import tmall.entity.PropertyValue;

import java.util.List;

public interface PropertyValueService {
    void init(Product p);

    List<PropertyValue> selectPropertyValueList(Integer pid);

    PropertyValue selectPropertyValue(Integer pid, Integer ptid);

    void addPropertyValue(PropertyValue propertyValue);

    void updatePropertyValueById(PropertyValue propertyValue);

    void deletePropertyValueById(Integer id);
}
