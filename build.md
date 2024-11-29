# 手動Build手順

``` sh
./gradlew bootBuildImage --imageName=ablankz/nova-user-preference-service:1.0.0
docker push ablankz/nova-user-preference-service:1.0.0
```