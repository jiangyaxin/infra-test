package com.jyx.feature.test.cqengine;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author jiang
 * @since 2023/2/4 17:10
 */
public class CqEngineTest {

    @Test
    public void basicTest() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<>();

        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(ReversedRadixTreeIndex.onAttribute(Car.NAME));
        cars.addIndex(SuffixTreeIndex.onAttribute(Car.DESCRIPTION));
        cars.addIndex(HashIndex.onAttribute(Car.FEATURES));

        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));

        Query<Car> query = or(endsWith(Car.NAME, "vic"), lessThan(Car.CAR_ID, 2));
        cars.retrieve(query).forEach(System.out::println);

        Thresholds thresholds = new Thresholds(Arrays.asList(new Threshold(EngineThresholds.INDEX_ORDERING_SELECTIVITY, 1.0D)));
        OrderByOption<Car> orderBy = orderBy(descending(Car.CAR_ID));
        QueryOptions queryOption = queryOptions(orderBy, thresholds);
        queryOption.put(QueryLog.class,true);
        cars.retrieve(lessThan(Car.CAR_ID, 2)).forEach(System.out::println);

    }
}
