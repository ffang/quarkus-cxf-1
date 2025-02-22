package io.quarkiverse.cxf.deployment;

import java.util.stream.Stream;

import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

/**
 * {@link BuildStep}s related to {@code org.glassfish.jaxb:*}.
 */
class JaxbProcessor {

    @BuildStep
    void indexDependencies(BuildProducer<IndexDependencyBuildItem> indexDependencies) {
        Stream.of(
                "org.glassfish.jaxb:txw2",
                "org.glassfish.jaxb:jaxb-runtime")
                .forEach(ga -> {
                    String[] coords = ga.split(":");
                    indexDependencies.produce(new IndexDependencyBuildItem(coords[0], coords[1]));
                });
    }

    @BuildStep
    void registerWsSecurityReflectionItems(CombinedIndexBuildItem combinedIndexBuildItem,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        final IndexView index = combinedIndexBuildItem.getIndex();

        Stream.of(
                "jakarta.xml.bind.JAXBElement",
                "com.sun.xml.bind.v2.runtime.JaxBeanInfo",
                "org.glassfish.jaxb.runtime.v2.runtime.JaxBeanInfo")
                .flatMap(className -> index.getAllKnownSubclasses(DotName.createSimple(className)).stream())
                .map(classInfo -> classInfo.name().toString())
                .map(className -> ReflectiveClassBuildItem.builder(className).methods().build())
                .forEach(reflectiveClass::produce);

        reflectiveClass.produce(ReflectiveClassBuildItem.builder(
                "org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl",
                "org.glassfish.jaxb.runtime.v2.runtime.JaxBeanInfo").methods().build());

    }

}
