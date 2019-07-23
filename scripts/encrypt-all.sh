cd .config
sops -e --encrypted-suffix enc nonprod.dec.yaml > nonprod.enc.yaml
sops -e --encrypted-suffix enc prod.dec.yaml > prod.enc.yaml
cd ..