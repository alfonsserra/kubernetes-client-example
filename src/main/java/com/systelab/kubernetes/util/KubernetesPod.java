package com.systelab.kubernetes.util;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesPod {


    public static void printPod(Pod pod) {
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

    private static  void printContainers(List<Container> containers) {
        System.out.println("Containers:");
        containers.forEach(c -> printContainer(c));
    }

    private static  void printContainer(Container container) {
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

    public static List<Pod> getPods(KubernetesClient client, String nameSpace) throws KubernetesClientException {
        return client.pods().inNamespace(nameSpace).list().getItems();
    }
}
