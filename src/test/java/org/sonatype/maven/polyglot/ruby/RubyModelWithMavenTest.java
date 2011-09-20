package org.sonatype.maven.polyglot.ruby;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.guice.bean.containers.InjectedTestCase;

public class RubyModelWithMavenTest extends InjectedTestCase {

    @Inject
    @Named("${basedir}/src/test/poms")
    private File poms;

    public void testRubyModelWriter() throws Exception {
        File pom = new File(poms, "maven-parent-pom.xml");
        MavenXpp3Reader xmlModelReader = new MavenXpp3Reader();
        Model xmlModel = xmlModelReader.read(new FileInputStream(pom));
        
        //
        // Write out the Ruby POM
        //
        ModelWriter writer = new RubyModelWriter();
        StringWriter w = new StringWriter();
        writer.write(w, new HashMap<String, Object>(), xmlModel);

        // Let's take a look at see what's there
        System.out.println(w.toString());

        //
        // Read in the Ruby POM
        //
        ModelReader rubyModelReader = new RubyModelReader();
        StringReader r = new StringReader(w.toString());
        Model rubyModel = rubyModelReader
                .read(r, new HashMap<String, Object>());
        //
        // Test for fidelity
        //
        assertNotNull(rubyModel);
        testMavenModelForCompleteness(rubyModel);

    }

    void testMavenModelForCompleteness(Model model) {
        //
        // name, url
        //
        assertEquals("Apache Maven", model.getName());
        assertEquals("http://maven.apache.org/", model.getUrl());
        //
        // gav
        //
        assertEquals("org.apache.maven:maven:3.0.4-SNAPSHOT", model
                .getGroupId()
                + ":" + model.getArtifactId() + ":" + model.getVersion());
        //
        // packaging
        //
        assertEquals("pom", model.getPackaging());
        //
        // description
        //
        assertTrue(model.getDescription().length() > 200);

        //    
        // repos
        //
        assertEquals(0, model.getRepositories().size());
        assertEquals(0, model.getPluginRepositories().size());
        //
        // parent
        //
        assertEquals("org.eclipse.tesla:tesla:3", model.getParent()
                .getGroupId()
                + ":"
                + model.getParent().getArtifactId()
                + ":"
                + model.getParent().getVersion());
        //
        // properties
        //
        assertEquals("1.7", model.getProperties().getProperty("gossipVersion"));
        assertEquals("1.12", model.getProperties().getProperty("aetherVersion"));
        assertEquals("4.8.2", model.getProperties().getProperty("junitVersion"));
        assertEquals("2.0.6", model.getProperties().getProperty(
                "plexusUtilsVersion"));
        assertEquals("2.4", model.getProperties().getProperty(
                "classWorldsVersion"));
        assertEquals("UTF-8", model.getProperties().getProperty(
                "project.build.sourceEncoding"));
        assertEquals("yyyyMMddHHmm", model.getProperties().getProperty(
                "maven.build.timestamp.format"));
        assertEquals("2.2.1", model.getProperties().getProperty(
                "sisuInjectVersion"));
        assertEquals("1.4.1", model.getProperties().getProperty(
                "modelloVersion"));
        assertEquals("Eclipse Tesla", model.getProperties().getProperty(
                "distributionName"));
        assertEquals("1.5.5", model.getProperties()
                .getProperty("plexusVersion"));
        assertEquals("3.0.4-SNAPSHOT", model.getProperties().getProperty(
                "mavenVersion"));
        assertEquals("1.2_Java1.3", model.getProperties().getProperty(
                "easyMockVersion"));
        assertEquals("2.3", model.getProperties().getProperty("jlineVersion"));
        assertEquals("1.3", model.getProperties().getProperty("jxpathVersion"));
        assertEquals("true", model.getProperties().getProperty(
                "maven.test.redirectTestOutputToFile"));
        assertEquals("1.2", model.getProperties().getProperty(
                "commonsCliVersion"));
        assertEquals("UTF-8", model.getProperties().getProperty(
                "project.reporting.outputEncoding"));
        assertEquals("1.14", model.getProperties().getProperty(
                "plexusInterpolationVersion"));
        assertEquals("Tesla", model.getProperties().getProperty(
                "distributionShortName"));
        assertEquals("1.0-beta-7", model.getProperties().getProperty(
                "wagonVersion"));
        assertEquals("${maven.build.timestamp}", model.getProperties()
                .getProperty("build.timestamp"));
        assertEquals("1.3", model.getProperties().getProperty(
                "securityDispatcherVersion"));
        assertEquals("eclipse-tesla", model.getProperties().getProperty(
                "distributionId"));
        assertEquals("1.6.1", model.getProperties().getProperty("slf4jVersion"));
        assertEquals("1.7", model.getProperties().getProperty("cipherVersion"));
        assertEquals("3.0.4-SNAPSHOT", model.getProperties().getProperty(
                "gshellVersion"));
        //
        // dependencies
        //
        assertEquals("junit:junit:${junitVersion}", gav(model.getDependencies()
                .get(0)));
        assertEquals("test", model.getDependencies().get(0).getScope());
        //
        // dependencyManager
        //
        assertEquals("org.apache.maven:maven-model:${project.version}",
                gav(model.getDependencyManagement().getDependencies().get(0)));
        assertEquals("org.apache.maven:maven-core:${project.version}",
                gav(model.getDependencyManagement().getDependencies().get(5)));
        assertEquals(
                "org.sonatype.sisu:sisu-inject-plexus:${sisuInjectVersion}",
                gav(model.getDependencyManagement().getDependencies().get(12)));
        assertEquals("org.slf4j:slf4j-simple:${slf4jVersion}", gav(model
                .getDependencyManagement().getDependencies().get(18)));
        assertEquals("runtime", model.getDependencyManagement()
                .getDependencies().get(18).getScope());
        assertEquals("commons-cli:commons-cli:${commonsCliVersion}", gav(model
                .getDependencyManagement().getDependencies().get(31)));
        assertEquals(2, model.getDependencyManagement().getDependencies().get(
                31).getExclusions().size());
        assertEquals("commons-lang:commons-lang", gav(model
                .getDependencyManagement().getDependencies().get(31)
                .getExclusions().get(0)));
        assertEquals("commons-logging:commons-logging", gav(model
                .getDependencyManagement().getDependencies().get(31)
                .getExclusions().get(1)));
        assertEquals("org.sonatype.jline:jline:${jlineVersion}", gav(model
                .getDependencyManagement().getDependencies().get(61)));
        assertEquals("tests", model.getDependencyManagement().getDependencies()
                .get(61).getClassifier());
        //
        // pluginManager
        //
        Plugin p = model.getBuild().getPluginManagement().getPlugins().get(0);
        assertEquals(
                "org.codehaus.plexus:plexus-component-metadata:${plexusVersion}",
                gav(p));
        assertNull(p.getConfiguration());
        p = model.getBuild().getPluginManagement().getPlugins().get(4);
        assertEquals(
                "org.codehaus.modello:modello-maven-plugin:${modelloVersion}",
                gav(p));
        assertNotNull(p.getConfiguration());
        assertEquals("true", ((Xpp3Dom) p.getConfiguration()).getChild(
                "useJava5").getValue());
        //
        // modules
        //
        assertEquals("maven-core", model.getModules().get(0));
        assertEquals("apache-maven", model.getModules().get(1));
        assertEquals("maven-model", model.getModules().get(2));
        assertEquals("maven-settings", model.getModules().get(3));
        assertEquals("maven-settings-builder", model.getModules().get(4));
        assertEquals("maven-artifact", model.getModules().get(5));
        assertEquals("maven-aether-provider", model.getModules().get(6));
        assertEquals("maven-repository-metadata", model.getModules().get(7));
        assertEquals("maven-plugin-api", model.getModules().get(8));
        assertEquals("maven-model-builder", model.getModules().get(9));
        assertEquals("maven-embedder", model.getModules().get(10));
        assertEquals("maven-compat", model.getModules().get(11));
        assertEquals("tesla-shell", model.getModules().get(12));
        assertEquals("tesla-polyglot", model.getModules().get(13));
        //
        // plugins
        //
        p = model.getBuild().getPlugins().get(0);
        assertEquals("org.codehaus.mojo:animal-sniffer-maven-plugin:1.6",
                gav(p));
        assertNotNull(p.getConfiguration());
        assertEquals("TODO", ((Xpp3Dom) p.getConfiguration()).getChild(
                "signature").getValue());
        p = model.getBuild().getPlugins().get(1);
        assertEquals("org.sonatype.plugins:sisu-maven-plugin:null", gav(p));
        assertNull(p.getConfiguration());
        p = model.getBuild().getPlugins().get(2);
        assertEquals(
                "com.mycila.maven-license-plugin:maven-license-plugin:1.9.0",
                gav(p));
        assertNotNull(p.getConfiguration());
        assertEquals("true", ((Xpp3Dom) p.getConfiguration()).getChild(
                "aggregate").getValue());
        assertEquals("true", ((Xpp3Dom) p.getConfiguration()).getChild(
                "strictCheck").getValue());
        assertEquals("false", ((Xpp3Dom) p.getConfiguration()).getChild(
                "useDefaultExcludes").getValue());
        assertEquals("${project.basedir}/header.txt", ((Xpp3Dom) p
                .getConfiguration()).getChild("header").getValue());
        assertEquals("TODO", ((Xpp3Dom) p.getConfiguration()).getChild(
                "excludes").getValue());
        assertEquals("TODO", ((Xpp3Dom) p.getConfiguration()).getChild(
                "includes").getValue());
        assertEquals("TODO", ((Xpp3Dom) p.getConfiguration()).getChild(
                "mapping").getValue());
    }

    String gav(Dependency d) {
        return d.getGroupId() + ":" + d.getArtifactId() + ":" + d.getVersion();
    }

    String gav(Plugin p) {
        return p.getGroupId() + ":" + p.getArtifactId() + ":" + p.getVersion();
    }

    String gav(Exclusion e) {
        return e.getGroupId() + ":" + e.getArtifactId();
    }
}
