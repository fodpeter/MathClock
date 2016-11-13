#!/usr/bin/python

import os
import requests
from collections import defaultdict
from glob import glob


def convert(dpi, equation):
    url = 'http://latex.codecogs.com/png.latex?'
    r = requests.get(url, params='\dpi{' + str(dpi) + '} ' + equation)
    print('HTTP response: ' + str(r.status_code))
    if (r.status_code != 200):
        raise "error in " + equation
    return r.content


def main():
    equations = readEquations('equations.txt')
    outputDir = 'output'
    mappingFile = outputDir + '/mapping.properties'
    if not os.path.exists(outputDir):
        os.mkdir(outputDir)
    else:
        for filename in glob(outputDir + "/*/*.png"):
            os.remove(filename)
        if os.path.exists(mappingFile):
            os.remove(mappingFile)
    dpis = [300, 600]
    for dpi in dpis:
        dir = outputDir + '/' + str(dpi)
        if not os.path.exists(dir):
            os.mkdir(dir)
    with open(mappingFile, 'w') as mapping:
        for k, v in sorted(equations.items()):
            ki = 0
            for equation in sorted(v):
                print('converting' + str(k) + '  =  ' + equation)
                filename = 'eq' + str(k) + '_' + str(ki)
                for dpi in dpis:
                    convertAndWrite(outputDir, filename, equation, dpi)
                mapping.write(filename + '=' + str(k) + '\n')
                ki = ki + 1


def convertAndWrite(outputDir, filename, w, dpi):
    data = convert(dpi, w)
    writeImage(outputDir + '/' + str(dpi) + '/' + filename + '.png', data)


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
                if (len(key) > 0 and len(value) > 0):
                    md[int(key)].append(value)
    return md


main()
