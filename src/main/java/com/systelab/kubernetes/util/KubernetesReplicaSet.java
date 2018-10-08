package com.systelab.kubernetes.util;

import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesReplicaSet {

    public static void printReplicaSet(ReplicaSet replicaSet) {

        System.out.println("Replica Set Details");
        System.out.println("-------------------------------------");

        System.out.println("Name:\t\t\t" + replicaSet.getMetadata().getName());
        System.out.println("Namespace:\t\t" + replicaSet.getMetadata().getNamespace());
        System.out.println("Labels:\t\t\t" + replicaSet.getMetadata().getLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Annotations:\t" + replicaSet.getMetadata().getAnnotations().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Creation Time:\t" + replicaSet.getMetadata().getCreationTimestamp());
        System.out.println("Selector:\t" + replicaSet.getSpec().getSelector().getMatchLabels().values().stream().collect(Collectors.joining(", ")));
        System.out.println("Replicas:\t" + replicaSet.getSpec().getReplicas());
        System.out.println("");
    }

    public static List<ReplicaSet> getReplicaSets(KubernetesClient client, String nameSpace) throws KubernetesClientException {
        return client.apps().replicaSets().inNamespace(nameSpace).list().getItems();
    }
}
