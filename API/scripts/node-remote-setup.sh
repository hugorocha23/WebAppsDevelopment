gcloud compute scp spring.service daw-api-instance-2:~
gcloud compute scp build/libs/API-0.0.1-SNAPSHOT.jar daw-api-instance-2:~
gcloud compute scp scripts/node-local-setup.sh daw-api-instance-2:~
gcloud compute ssh daw-api-instance-2 --command "sudo sh node-local-setup.sh"
