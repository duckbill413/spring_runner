/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.learner.util;

import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class MenuFixtureFactory {
    // https://github.com/j-easy/easy-random
    public static EasyRandomParameters getMenuParams() {
        var parameter = new EasyRandomParameters()
                .excludeField(named("id"))
                .stringLengthRange(5, 20) // name은 20글자 이내
                .randomize(named("price").and(ofType(Long.class)), new LongRangeRandomizer(1000L, 100000L))
                .randomize(named("stock").and(ofType(Long.class)), new LongRangeRandomizer(0L, 100L));

        return parameter;
    }
}
