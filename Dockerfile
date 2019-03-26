FROM openjdk:8u181-jdk

# install atlassian-sdk
RUN apt-get update \
 && apt-get install apt-transport-https \
 && echo "deb https://packages.atlassian.com/atlassian-sdk-deb stable contrib" >>/etc/apt/sources.list \
 && wget -O- https://packages.atlassian.com/api/gpg/key/public | apt-key add - \
 && apt-get update \
 && apt-get install atlassian-plugin-sdk \
 && apt-get clean
