package cz.muni.fi.pa165.mushrooms.sample;

import java.io.IOException;

import javax.annotation.PostConstruct;

import cz.muni.fi.pa165.mushrooms.service.config.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(ServiceConfiguration.class)
@ComponentScan(basePackageClasses = {SampleDataLoadingFacadeImpl.class})
public class SampleDataConfiguration {

    final static Logger log = LoggerFactory.getLogger(SampleDataConfiguration.class);

    @Autowired
    SampleDataLoadingFacade sampleDataLoadingFacade;

    @PostConstruct
    public void dataLoading() throws IOException {
        log.debug("dataLoading()");
        sampleDataLoadingFacade.loadData();
    }

}
