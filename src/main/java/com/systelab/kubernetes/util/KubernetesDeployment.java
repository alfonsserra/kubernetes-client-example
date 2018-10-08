package com.systelab.kubernetes.util;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesDeployment {

    private KubernetesDeployment() {
        throw new AssertionError();
    }

    public static int getNumberOfPods(KubernetesClient client, String namespace, String deploymentName) throws KubernetesClientException {
        return client.apps().deployments().inNamespace(namespace).withName(deploymentName).get().getSpec().getReplicas();
    }

    public static void scaleDeployment(KubernetesClient client, String namespace, String name, int numberOfPods) throws KubernetesClientException {
        client.apps().deployments().inNamespace(namespace).withName(name).scale(numberOfPods);
    }

    public static List<Deployment> getList(KubernetesClient client, String nameSpace) throws KubernetesClientException {
        return client.apps().deployments().inNamespace(nameSpace).list().getItems();
    }

    public static void print(Deployment deployment) {

        System.out.println("Deployment Details");
        System.out.println("-------------------------------------");

        System.out.println("Name:\t\t" + deployment.getMetadata().getName());
        System.out.println("Namespace:\t" + deployment.getMetadata().getNamespace());
        System.out.println("Labels:\t\t" + deployment.getMetadata().getLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Annotations:\t" + deployment.getMetadata().getAnnotations().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Creation Time:\t" + deployment.getMetadata().getCreationTimestamp());
        System.out.println("Selector:\t" + deployment.getSpec().getSelector().getMatchLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Replicas:\t" + deployment.getSpec().getReplicas());
        System.out.println("");
    }

}
