package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.configuration.DataImportJobConfiguration;
import ch.bernmobil.vibe.staticdata.configuration.SpringConfig;
import ch.bernmobil.vibe.staticdata.processor.TestConfiguration;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class, SpringConfig.class})
@ActiveProfiles("testConfiguration")
public class AreaImportTest {

    private DSLContext dslContext;

    @Test
    public void testSqlString() {
        AreaImport areaImport = new AreaImport(null, dslContext, null);
        //assertThat(query, is("INSERT INTO area(id, name, update) VALUES (?, ?, ?)"));
    }

    @Autowired
    public void setDslContext(@Qualifier("StaticDslContext") DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}