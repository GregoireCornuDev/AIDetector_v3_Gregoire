import os
import glob
import click
from tqdm import tqdm

def list_long_wav_files(directory, duration_threshold, output_file):
    wav_files = glob.glob(os.path.join(directory, '**/*.wav'), recursive=True)

    long_wav_files = []
    
    with tqdm(total=len(wav_files), desc='Parsing directory') as pbar:
        for wav_file in wav_files:
            duration = get_wav_duration(wav_file)
            if duration > duration_threshold:
                long_wav_files.append(wav_file)
            pbar.update(1)

    with open(output_file, 'w') as file:
        for file_path in long_wav_files:
            file.write(file_path + '\n')

def get_wav_duration(wav_file):
    try:
        import soundfile as sf
        data, sample_rate = sf.read(wav_file)
        duration = len(data) / float(sample_rate)
        return duration
    except ImportError:
        import wave
        with wave.open(wav_file, 'rb') as wav:
            frames = wav.getnframes()
            rate = wav.getframerate()
            duration = frames / float(rate)
            return duration

@click.command()
@click.argument('directory', type=click.Path(exists=True, file_okay=False, resolve_path=True))
@click.argument('out_file', type=click.Path(exists=False, dir_okay=False, resolve_path=True))
@click.option('-d', '--duration_threshold', default=1, required=False, type=int)
def main(directory, out_file, duration_threshold):
    duration_threshold = 20.0  # Minimum duration in seconds
    list_long_wav_files(directory, duration_threshold, out_file)

if __name__ == '__main__':
        main()
