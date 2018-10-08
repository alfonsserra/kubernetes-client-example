mvn clean package
docker build -t systelab/kubernetes-client .
docker push systelab/kubernetes-client
kubectl delete -f fabric8-rbac.yaml
kubectl apply -f fabric8-rbac.yaml
kubectl delete deploy kubernetes-client-deploy
kubectl create -f kubernetes-client-deploy.yml