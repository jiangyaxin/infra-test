package com.jyx.feature.test.cqengine;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author jiang
 * @since 2023/2/4 17:13
 */
@Data
@AllArgsConstructor
public class Car {

    private Integer carId;

    private String name;

    private String description;

    private List<String> features;

    public static final SimpleAttribute<Car, Integer> CAR_ID = new SimpleAttribute<>("carId") {
        @Override
        public Integer getValue(Car o, QueryOptions queryOptions) {
            return o.getCarId();
        }
    };

    public static final SimpleAttribute<Car, String> NAME = new SimpleAttribute<>("name") {
        @Override
        public String getValue(Car o, QueryOptions queryOptions) {
            return o.getName();
        }
    };

    public static final SimpleAttribute<Car, String> DESCRIPTION = new SimpleAttribute<>("description") {
        @Override
        public String getValue(Car o, QueryOptions queryOptions) {
            return o.getDescription();
        }
    };

    public static final SimpleAttribute<Car, List<String>> FEATURES = new SimpleAttribute<>("features") {
        @Override
        public List<String> getValue(Car o, QueryOptions queryOptions) {
            return o.getFeatures();
        }
    };

}
