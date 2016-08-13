#!/usr/bin/python

from collections import defaultdict

import os
import requests
from glob import glob

def convert(equation):
    url = 'http://latex.codecogs.com/png.latex?'
    r = requests.get(url, params='\dpi{300} '+equation)
    print(r.status_code)
    return r.content

def main():
    equations = readEquations('equations.txt')
    if (not os.path.exists('output')):
        os.mkdir('output')
    else:
        for filename in glob("output/*.png"):
            os.remove(filename);
        if (os.path.exists('output/mapping.properties')):
            os.remove('output/mapping.properties')
    with open('output/mapping.properties', 'w') as mapping:
        for k, v in equations.items():
            ki=0;
            for w in v:
                print('converting' +k+'  =  ' +w)
                data=convert(w)
                filename='eq'+k+'_'+str(ki)
                writeImage('output/' + filename + '.png', data);
                mapping.write(filename+'='+k+'\n')
                ki=ki+1

def writeImage(filename, data):
    with open(filename, 'w') as f:
        f.write(data)

def readEquations(filepath):
    md = defaultdict(list)
    with open(filepath, "rt") as f:
        for line in f:
            l = line.strip()
            if l and not l.startswith('#'):
                key_value = l.split('=')
                key = key_value[0].strip()
                value = '='.join(key_value[1:]).strip('" \t')
                if (len(key)>0 and len(value)>0):
                     md[key].append(value)
    return md

main()

