#!/bin/bash

echo ">>>> >>>> >>>> >>>> ENTERING SETUP.SH <<<< <<<< <<<< <<<< "

## update and install some things we should probably have
apt-get update
apt-get install -y \
  curl \
  apt-utils \
  git \
  gnupg2 \
  jq \
  sudo \
  zsh \
  ca-certificates \
  groff \
  less \
  openjdk-8-jdk


# Python related tools
echo "Installing PYTHON..."

apt-get install -y software-properties-common
add-apt-repository ppa:deadsnakes/ppa
apt-get update && apt-get install -y python3.7 python3-pip

python3.7 -m pip install pip

apt-get update && apt-get install -y \
  python3-distutils \
  python3-setuptools

python3.7 -m pip install pip --upgrade pip awscli

echo "Installing PYTHON - DONE!"


echo "Installing oh-my-zsh"

sh -c "$(curl -fsSL https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
cp -R /root/.oh-my-zsh /home/$USERNAME
cp /root/.zshrc /home/$USERNAME
sed -i -e "s/\/root\/.oh-my-zsh/\/home\/$USERNAME\/.oh-my-zsh/g" /home/$USERNAME/.zshrc
chown -R $USER_UID:$USER_GID /home/$USERNAME/.oh-my-zsh /home/$USERNAME/.zshrc

echo "Installing oh-my-zsh - DONE"



echo ">>>> >>>> >>>> >>>> EXITING SETUP.SH <<<< <<<< <<<< <<<< "
