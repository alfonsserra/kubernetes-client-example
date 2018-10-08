package com.systelab.kubernetes;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesClientTest {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClientTest.class);
    private final KubernetesClient client;

    KubernetesClientTest() {
    //    String master = "https://192.168.99.100:8443/";
    //    String master = "https://kubernetes/";
        //    Config config = new ConfigBuilder().withNamespace("default").withMasterUrl(master).build();

        Config config = new ConfigBuilder().build();
        client = new DefaultKubernetesClient(config);
    }

    public void printReplicaSets() {
        try {
            getReplicaSets("default").forEach(rs -> printReplicaSet(rs));

        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void printReplicaSet(ReplicaSet replicaSet) {
        System.out.println("Replica Set Details");
        System.out.println("-------------------------------------");

        System.out.println("Name:\t\t\t" + replicaSet.getMetadata().getName());
        System.out.println("Namespace:\t\t" + replicaSet.getMetadata().getNamespace());
        System.out.println("Labels:\t\t\t" + replicaSet.getMetadata().getLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Annotations:\t" + replicaSet.getMetadata().getAnnotations().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Creation Time:\t" + replicaSet.getMetadata().getCreationTimestamp());
        System.out.println("Selector:\t" + replicaSet.getSpec().getSelector().getMatchLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("");
    }

    public void printPods() {
        try {
            getPods("default").forEach(p -> printPod(p));

        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void printPod(Pod pod) {
        System.out.println("Pod Details");
        System.out.println("-------------------------------------");

        System.out.println("Name:\t\t\t" + pod.getMetadata().getName());
        System.out.println("Namespace:\t\t" + pod.getMetadata().getNamespace());
        System.out.println("Labels:\t\t\t" + pod.getMetadata().getLabels().values().stream().collect(Collectors.joining(", ")));

        System.out.println("Creation Time:\t" + pod.getMetadata().getCreationTimestamp());
        System.out.println("Status:\t\t\t" + pod.getStatus().getPhase());
        System.out.println("QoS Class:\t\t" + pod.getStatus().getQosClass());

        printContainers(pod.getSpec().getContainers());
        System.out.println("");
    }

    private void printService(Service service) {
        System.out.println("Service Details");
        System.out.println("-------------------------------------");

        System.out.println("Name:\t\t\t" + service.getMetadata().getName());
        System.out.println("Namespace:\t\t" + service.getMetadata().getNamespace());
        System.out.println("Labels:\t\t\t" + service.getMetadata().getLabels().values().stream().collect(Collectors.joining(", ")));

        System.out.println("Creation Time:\t" + service.getMetadata().getCreationTimestamp());
        if (service.getSpec().getSelector() != null) {
            System.out.println("Selector:\t\t" + service.getSpec().getSelector().values().stream().collect(Collectors.joining(", ")));
        }

        System.out.println("Type:\t\t\t" + service.getSpec().getType());
        System.out.println("Session Aff:\t" + service.getSpec().getSessionAffinity());

        System.out.println("");
    }

    private void printContainers(List<Container> containers) {
        System.out.println("Containers:");
        containers.forEach(c -> printContainer(c));
    }

    private void printContainer(Container container) {
        System.out.println("\t" + container.getName());
        System.out.println("\t\tImage:\t\t" + container.getImage());
        if (container.getEnv().size() > 0) {
            System.out.println("\t\tEnvironment:");
            container.getEnv().forEach((e) -> System.out.println("\t\t\t\t\t" + e.getName() + ": " + e.getValue()));
        }
        if (container.getPorts().size() > 0) {
            System.out.println("\t\tPorts:");
            container.getPorts().forEach((p) -> System.out.println("\t\t\t\t\t" + p.getContainerPort() + ":" + p.getHostPort()));
        }
    }

    public void printServices() {
        try {
            getServices("default").forEach(s -> printService(s));

        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<Service> getServices(String nameSpace) {
        return client.services().inNamespace(nameSpace).list().getItems();
    }

    private List<Pod> getPods(String nameSpace) {
        return client.pods().inNamespace(nameSpace).list().getItems();
    }

    private List<ReplicaSet> getReplicaSets(String nameSpace) {
        return client.apps().replicaSets().inNamespace(nameSpace).list().getItems();
    }

    public void close() {
        client.close();
    }

    public static void main(String... args) {
        KubernetesClientTest test = new KubernetesClientTest();
        test.printPods();
        // test.printReplicaSets();
        // test.printServices();
        test.close();
    }
}
