package com.systelab.kubernetes;

import com.systelab.kubernetes.util.KubernetesDeployment;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubernetesClientTest implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClientTest.class);
    private final KubernetesClient client;

    KubernetesClientTest() {
        // In local heads to https://192.168.99.100:8443/ and inside Kubernetes heads to https://kubernetes/
        Config config = new ConfigBuilder().build();
        client = new DefaultKubernetesClient(config);
    }

    @Override
    public void run() {
        final String namespace="default";

        setupWatcher(namespace);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                try {
                    KubernetesDeployment.scaleDeployment(client, namespace, "kubernetes-client-deploy", KubernetesDeployment.getNumberOfPods(client, namespace, "kubernetes-client-deploy") + 1);
                    KubernetesDeployment.getList(client, namespace).forEach(d -> KubernetesDeployment.print(d));
                } catch (KubernetesClientException e) {
                    logger.error(e.getMessage(), e);
                }
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        client.close();
    }

    public void setupWatcher(String namespace) {
        client.events().inNamespace(namespace).watch(new Watcher<Event>() {
            @Override
            public void eventReceived(Action action, Event resource) {
                System.out.println("event " + action.name() + " " + resource.toString());
            }

            @Override
            public void onClose(KubernetesClientException cause) {
                System.out.println("Watcher close due to " + cause);
            }

        });
    }

    public static void main(String... args) {
        new Thread(new KubernetesClientTest()).start();
    }
}
