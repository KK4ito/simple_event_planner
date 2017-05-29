#!/bin/sh
npm i -g ionic@latest
cd ionic && npm i && cd ..
./restart.sh
cd ionic && ionic serve