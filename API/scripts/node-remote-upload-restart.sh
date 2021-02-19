gcloud compute scp build/libs/API-0.0.1-SNAPSHOT.jar daw-api-instance-2:~
gcloud compute ssh daw-api-instance-2 --command 'sudo cp API-0.0.1-SNAPSHOT.jar /var/spring'
gcloud compute ssh daw-api-instance-2 --command 'sudo systemctl restart spring'
