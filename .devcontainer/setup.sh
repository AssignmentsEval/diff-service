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



echo ">>>> >>>> >>>> >>>> EXITING SETUP.SH <<<< <<<< <<<< <<<< "
