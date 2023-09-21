import numpy as np
from numpy import fft
import sys


def fourierExtrapolation(x, n_predict):
    n = x.size
    harmonics = 10  #
    n_harm = harmonics  # number of harmonics in model
    t = np.arange(0, n)
    p = np.polyfit(t, x, 1)  # find linear trend in x
    x_notrend = x - p[0] * t  # detrended x
    x_freqdom = fft.fft(x_notrend)  # detrended x in frequency domain
    f = fft.fftfreq(n)  # frequencies
    indexes = list(range(n))
    # sort indexes by frequency, lower -> higher
    indexes.sort(key=lambda i: np.absolute(f[i]))

    t = np.arange(0, n + n_predict)
    restored_sig = np.zeros(t.size)
    for i in indexes[:1 + n_harm * 2]:
        ampli = np.absolute(x_freqdom[i]) / n  # amplitude
        phase = np.angle(x_freqdom[i])  # phase
        restored_sig += ampli * np.cos(2 * np.pi * f[i] * t + phase)
    return restored_sig + p[0] * t

if __name__ == "__main__":
    input_str=sys.argv[1]
    #print(input_str)
    #input_str="249,757,1385,1489,1717,1721,2323,24,24,23,24,32323,23,2332,3,42,4,2323"
    splits = input_str.strip().split(',')  # strip()默认移除字符串首尾空格或换行符
    read_data = [float(x) for x in splits[:]]
    #print(read_data)
    #datamat[:] = splits[:]
    #med_trace_list = [249, 757, 1385, 1489, 1717, 1721, 2323, 24, 24,23,24,32323,23,2332,3,42,4,2323]
    training_trace=np.array(read_data)
    n_predict = 1
    extrapolation = fourierExtrapolation(training_trace, n_predict)
    pred_value=extrapolation[len(extrapolation)-1]
    print(pred_value)