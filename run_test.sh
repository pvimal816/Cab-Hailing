#! /bin/sh
cd tests
for f in *.sh; do
  sh "$f" || break
  echo ""
done