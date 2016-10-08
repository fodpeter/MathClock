#!/bin/bash
cp output/mapping.properties ../app/src/main/assets/

rm ../app/src/main/res/drawable-hdpi/eq*.png
rm ../app/src/main/res/drawable-mdpi/eq*.png

cp output/600/eq*.png ../app/src/main/res/drawable-hdpi/
cp output/300/eq*.png ../app/src/main/res/drawable-mdpi/
