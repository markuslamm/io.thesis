#!/bin/sh

rm main.pdf main.bbl main.blg settings.aux main.acn main.aux main.bcf main.glo main.idx main.ilg main.ind main.ist main.lof main.log main.lol main.lot main.out main.run.xml main.synctex.gz main.toc

lualatex -synctex=1 -interaction=nonstopmode main && \
biber main && \
makeindex main  && \
makeindex -s main.ist -t main.alg -o main.acr main.acn  && \
makeindex -s main.ist -t main.glg -o main.gls main.glo  && \
lualatex -synctex=1 -interaction=nonstopmode main  && \
lualatex -synctex=1 -interaction=nonstopmode main