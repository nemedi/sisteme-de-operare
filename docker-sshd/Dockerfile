FROM ubuntu:22.04
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && \
    apt-get install -y openssh-server openssl mc sudo && \
    mkdir /var/run/sshd
RUN useradd -m -s /usr/bin/bash iulian && \
    echo "iulian:szervusz" | chpasswd && \
    usermod -aG sudo iulian
RUN mkdir -p /usr/local/setup
COPY *.sh /usr/local/setup
RUN chmod +x /usr/local/setup/*.sh
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config
VOLUME ["/usr/local/share"]
EXPOSE 22
ENTRYPOINT ["/usr/local/setup/entrypoint.sh"]