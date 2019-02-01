#!/bin/bash

eval "cat << EOF
$(<bintray.json)
EOF
" > bintray.json
