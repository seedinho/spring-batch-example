package org.cascomio.springbatchexample.writers;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.cascomio.springbatchexample.models.Property;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SparkPropertyWriter implements ItemWriter<Collection<Property>> {
    @Autowired
    private JavaSparkContext sparkContext;

    @Override
    public void write(List<? extends Collection<Property>> properties) throws Exception {
        properties.stream().forEach(p -> {
            try {
                JavaRDD<Property> propertiesRDD = sparkContext.parallelize(new ArrayList(p));
                propertiesRDD.map(prop -> prop.getAddress() + ";" + prop.getDescription())
                        .saveAsTextFile("hdfs://localhost:8020/batch/example.csv");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

    }
}
