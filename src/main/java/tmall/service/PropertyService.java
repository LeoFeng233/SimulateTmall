package tmall.service;

import tmall.entity.Property;

import java.util.List;

public interface PropertyService {
    List<Property> selectPropertyList(Integer cid);

    Property selectPropertyById(Integer id);

    void addProperty(Property property);

    void updatePropertyById(Property property);

    void deletePropertyById(Integer id);
}
