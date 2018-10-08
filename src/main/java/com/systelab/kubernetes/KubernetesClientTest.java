package com.systelab.kubernetes;

import com.systelab.kubernetes.util.KubernetesDeployment;
import com.systelab.kubernetes.util.KubernetesPod;
import com.systelab.kubernetes.util.KubernetesReplicaSet;
import com.systelab.kubernetes.util.KubernetesService;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubernetesClientTest implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesClientTest.class);
    private final KubernetesClient client;
    private final String namespace;

    KubernetesClientTest(String namespace) {
        this.namespace = namespace;
        // In local heads to https://192.168.99.100:8443/ and inside Kubernetes heads to https://kubernetes/
        Config config = new ConfigBuilder().build();
        client = new DefaultKubernetesClient(config);
    }

    @Override
    public void run() {

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

        while (!Thread.currentThread().isInterrupted()) {
            try {
                this.scaleDeployment("kubernetes-client-deploy");
                this.printDeployments();
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        this.close();
    }

    public void printPods() {
        try {
            KubernetesPod.getList(client, namespace).forEach(p -> KubernetesPod.print(p));
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void printReplicaSets() {
        try {
            KubernetesReplicaSet.getList(client, namespace).forEach(rs -> KubernetesReplicaSet.print(rs));
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void printDeployments() {
        try {
            KubernetesDeployment.getList(client, namespace).forEach(d -> KubernetesDeployment.print(d));
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void printServices() {
        try {
            KubernetesService.getList(client, namespace).forEach(s -> KubernetesService.print(s));

        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void scaleDeployment(String name) {
        try {
            KubernetesDeployment.scaleDeployment(client, namespace, name, KubernetesDeployment.getNumberOfPods(client, namespace, name) + 1);
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void close() {
        client.close();
    }


    public static void main(String... args) {
        new Thread(new KubernetesClientTest("default")).start();
    }
}
