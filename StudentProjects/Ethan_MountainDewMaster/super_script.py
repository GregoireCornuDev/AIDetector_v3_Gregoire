import csv
import click
import pandas as pd
from tqdm import tqdm

CHOICES = {"train-clean-100":"english", "train-clean-360":"english", "mls_french":"french", "mls_german":"german", "mls_portuguese":"portuguese", "mls_polish":"polish", "mls_dutch":"dutch", "mls_spanish":"spanish", "mls_italian":"italian"}

def find_lang(text:str) -> str:
    for choice, lg in CHOICES.items():
        if choice in text:
            return lg
    exit(f"ERROR: String {text} must match one of the following choices: {CHOICES}")

@click.command()
@click.argument('csv_file', type=str)# type=click.File('r'))
@click.argument('wav_list_file', type=click.File('r'))
@click.argument('output_file', type=click.File('w'))
def match_lines(csv_file, wav_list_file, output_file):
    csvdata = {}
    with open(csv_file, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter='\t', quotechar='|')
        for row in reader:
            csvdata[row[0]] = row[1]

    # Read the lines of the WAV list into a list
    wav_list_lines = wav_list_file.readlines()

    # Create an empty list to store the matched lines
    matched_lines = []

    # Match each line of the CSV with the associated line in the WAV list with tqdm progress bar
    for basename, transcription in tqdm(csvdata.items(), total=len(csvdata)):
        found = False
        #print(f"{basename}")
        for line in wav_list_lines:
            if basename in line:
                line= line.strip()
                lg = find_lang(line)
                matched_lines.append([lg, basename, transcription])
                found = True
                # if "103_1241_000020_000005" in basename:
                #    print(f"OK = {lg}, {basename}, {transcription}")
                #    exit(1)
                break
        if not found:
            click.echo(f"Could not find a match for: {basename}")

    # Create a DataFrame with the matched lines and export it to a new CSV file
    matched_df = pd.DataFrame(matched_lines, columns=['Language', 'Basename', 'Transcript'])
    matched_df.to_csv(output_file, sep='\t', index=False)

    #with open('test.csv', 'w', newline='') as csvfile:
    #    fieldnames = ['Language', 'Basename', 'Transcript']
    #    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    #    writer.writeheader()
    #    for line in matched_lines:
    #        writer.writerow({'Language': line[0], 'Basename': line[1], 'Transcript': line[2]})

    print("Matching completed successfully!")


if __name__ == '__main__':
    match_lines()

