FROM java:8

# JSTに変更
RUN echo "Asia/Tokyo" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata

ADD haishinsan-1.0.0-SNAPSHOT.jar /opt/

EXPOSE 8080

WORKDIR /opt/
CMD ["java", "-Xms512m", "-Xmx1024m", "-jar", "-Dspring.profiles.active=mock", "haishinsan-1.0.0-SNAPSHOT.jar"]
