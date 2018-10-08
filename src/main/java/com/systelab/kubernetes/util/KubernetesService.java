package com.systelab.kubernetes.util;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

import java.util.List;
import java.util.stream.Collectors;

public class KubernetesService {

    public static void printService(Service service) {
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

    public static List<Service> getServices(KubernetesClient client, String nameSpace) throws KubernetesClientException {
        return client.services().inNamespace(nameSpace).list().getItems();
    }
}
