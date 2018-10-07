package com.systelab.kubernetes;

import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubernetesClientTest {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClientTest.class);
    private final KubernetesClient client;

    KubernetesClientTest() {
        String master = "https://192.168.99.100:8443/";
        Config config = new ConfigBuilder().withMasterUrl(master).build();
        client = new DefaultKubernetesClient(config);
    }

    public void listPods() {
        try {
            client.pods().list().getItems().stream().map(pod -> pod.getMetadata().getName()).forEach(System.out::println);

        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void close() {
        client.close();
    }

    public static void main(String... args) {

        KubernetesClientTest test = new KubernetesClientTest();
        test.listPods();
        test.close();

    }
}
