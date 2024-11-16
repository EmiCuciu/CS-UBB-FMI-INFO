#!/bin/bash
F=""
while [ -z "$F" ] || [ ! -f "$F" ] || [ ! -r "$F" ]; do
 read -p "Provide an existing and readable file path:" F
done

