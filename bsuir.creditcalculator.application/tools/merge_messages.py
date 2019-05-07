import sys
import json


def getDictionary(filePath):
    with open(filePath) as sourceFile:
        sourceDictionary = json.load(sourceFile)
        
        return sourceDictionary


def mergeDictionaries(source, destination):
    mergedDictionary = destination
    for key, value in source.items():
        if key not in mergedDictionary:
            mergedDictionary[key] = value

    return mergedDictionary


sourceFilepath = sys.argv[1]
destinationFilepaths = sys.argv[1:]
sourceDictionary = getDictionary(sourceFilepath)

for filePath in destinationFilepaths:
    destinationDictionary = getDictionary(filePath)
    mergedDictionary = mergeDictionaries(sourceDictionary, destinationDictionary)
    with open(filePath, 'w') as outputFile:
        json.dump(mergedDictionary, outputFile, sort_keys=True, ensure_ascii=False, indent=2)
