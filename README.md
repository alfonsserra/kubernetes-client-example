# Kubernetes Client Example

Kubernetes client example

## Getting Started

To get you started you can simply clone the `kubernetes-client-example` repository and install the dependencies:

### Prerequisites

You need [git][git] to clone the `kubernetes-client-example` repository.

You will need [Java™ SE Development Kit 8][jdk-download] and [Maven][maven].

### Clone `kubernetes-client-example`

Clone the `kubernetes-client-example` repository using git:

```bash
git clone https://github.com/systelab/kubernetes-client-example.git
cd kubernetes-client-example
```

### Install Dependencies

In order to install the dependencies you must run:

```bash
mvn install
```


## Run:

Run the application from your IDE.


## Other configuration:

### User "system:serviceaccount:default:default" cannot get at the cluster scope

You should bind service account system:serviceaccount:default:default (which is the default account bound to Pod) with role cluster-admin, just create a yaml (named like fabric8-rbac.yaml) with following contents:

``` yaml
# NOTE: The service account `default:default` already exists in k8s cluster.
# You can create a new account following like this:
#---
#apiVersion: v1
#kind: ServiceAccount
#metadata:
#  name: <new-account-name>
#  namespace: <namespace>

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: fabric8-rbac
subjects:
  - kind: ServiceAccount
    # Reference to upper's `metadata.name`
    name: default
    # Reference to upper's `metadata.namespace`
    namespace: default
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io
```
Then, apply it by running kubectl apply -f fabric8-rbac.yaml.

If you want unbind them, just run kubectl delete -f fabric8-rbac.yaml.


[git]: https://git-scm.com/
[maven]: https://maven.apache.org/download.cgi
[jdk-download]: http://www.oracle.com/technetwork/java/javase/downloads