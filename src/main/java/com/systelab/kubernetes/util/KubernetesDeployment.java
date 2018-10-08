package com.systelab.kubernetes.util;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

public class KubernetesDeployment {
    public static int getNumberOfPods(KubernetesClient client, String namespace, String name) throws KubernetesClientException {
        return client.apps().deployments().inNamespace(namespace).withName(name).get().getSpec().getReplicas();
    }

    public static void scaleDeployment(KubernetesClient client, String namespace, String name, int numberOfPods) throws KubernetesClientException {
        client.apps().deployments().inNamespace(namespace).withName(name).scale(numberOfPods);
    }
}
