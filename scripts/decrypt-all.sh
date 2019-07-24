cd .config
sops -d nonprod.enc.yaml > nonprod.dec.yaml
sops -d prod.enc.yaml > prod.dec.yaml
cd ..