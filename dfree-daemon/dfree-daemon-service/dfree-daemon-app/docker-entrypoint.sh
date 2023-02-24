#!/usr/bin/env bash

#
# /*
#  * Copyright (c) 2022. Jiangsu Hongwangweb Technology Co.,Ltd.
#  * Licensed under the private license, you may not use this file except you get the License.
#  */
#

cd /opt/app || exit
java -Xms32m -Xmx512m -jar dfree-daemon-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=${1}
