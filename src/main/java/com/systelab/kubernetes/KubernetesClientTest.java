package com.systelab.kubernetes;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesClientTest {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClientTest.class);
    private final KubernetesClient client;

    KubernetesClientTest() {
        String master = "https://192.168.99.100:8443/";
        Config config = new ConfigBuilder().withMasterUrl(master).build();
        client = new DefaultKubernetesClient(config);
    }

    public void printPods() {
        try {
            getPods("default").forEach((p) -> printPod(p));

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
        System.out.println("Status\t\t\t" + pod.getStatus().getPhase());
        System.out.println("QoS Class\t\t" + pod.getStatus().getQosClass());
        System.out.println("");

    }

    private List<Pod> getPods(String nameSpace) {
        return client.pods().inNamespace(nameSpace).list().getItems();
    }


    public void close() {
        client.close();
    }

    public static void main(String... args) {
        KubernetesClientTest test = new KubernetesClientTest();
        test.printPods();
        test.close();
    }
}
