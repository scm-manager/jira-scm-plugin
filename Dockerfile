FROM amazoncorretto:8u202

# install atlassian-sdk
RUN echo "[Artifactory]" > /etc/yum.repos.d/artifactory.repo \
 && echo "name=Artifactory" >> /etc/yum.repos.d/artifactory.repo \
 && echo "baseurl=https://packages.atlassian.com/atlassian-sdk-rpm/" >> /etc/yum.repos.d/artifactory.repo \
 && echo "enabled=1" >> /etc/yum.repos.d/artifactory.repo \
 && echo "gpgcheck=0" >> /etc/yum.repos.d/artifactory.repo \
 && yum updateinfo metadata \
 && yum install -y install atlassian-plugin-sdk \
 && yum clean all \
 && rm -rf /var/cache/yum

ENV LANG C.utf8
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto.x86_64
