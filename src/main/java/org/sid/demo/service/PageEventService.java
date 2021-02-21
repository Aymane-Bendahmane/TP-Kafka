package org.sid.demo.service;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.sid.demo.entities.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class PageEventService {
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input) ->{
            System.out.println("*******************");
            System.out.println(input.toString());
            System.out.println("*******************");

        };
    }


    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return ()-> new PageEvent("hello",
                Math.random()>0.5?"U1":"U2",
                LocalDate.now(),
                new Random().nextInt(9000)) ;

    }

    @Bean
    public Function<PageEvent,PageEvent> pageEventPageEventFunction()
    {
        return (in)->{
            in.setName("toto");
            in.setAlias("bobo");
            return in;
        };

    }
    @Bean
    public Function<KStream<String,PageEvent>,KStream<String,Long>> kStreamFunction()
    {
        return (input)->{
            return input
                    .filter((k,v)->v.getNbr()>100)
                    .map((k,v)->new KeyValue<>(v.getAlias(),0L))
                    .groupBy((k,v)->k, Grouped.with(Serdes.String(),Serdes.Long()))
                    .windowedBy(TimeWindows.of(5000))
                    .count(Materialized.as("page-count"))
                    .toStream()
                    .map((k,v)->new KeyValue<>(""+k.window().startTime()+k.window().endTime()+k.key(),v));
        };
    }
}
