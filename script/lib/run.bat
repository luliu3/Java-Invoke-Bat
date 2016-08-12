:: fucntion	: merge audio with repeated bgm-music using ffmpeg
:: author	: fwli2 
:: verion 	: 2016/08/11
:: guide 	: run.bat voice_audio bgm_audio out_audio

setlocal EnableDelayedExpansion

:: get adudio duration
for /f "delims=" %%i in ('"ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 %1"') do (set voice_dur=%%i)
set /a a=%voice_dur%
for /f "delims=" %%i in ('"ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 %2"') do (set bgm_dur=%%i)
set /a b=%bgm_dur%

:: bat can not handle float number so %a% need to add 1
set /a loops=(%a%+1+3)/%b%+1 
set /a loops_for_temp=(%a%+1+3)/%b%
echo %loops%

:: concat param string
set pre_str=
for /l %%i in (1,1,%loops%) do (set "pre_str=!pre_str![%%i:a]") 
echo %pre_str%

:: concat ffmpeg -i praram
set input_param=
for /l %%i in (1,1,%loops%) do (set "input_param=!input_param!-i %2 ") 
echo %input_param%

:: config fade time 
set /a fade_out_time=5
set /a fade_out_begin=(%a%+1+3)-%fade_out_time%
echo %fade_out_time%
echo %fade_out_begin%

:: ffmpeg process
if %loops% EQU 1 (
	echo 1
	ffmpeg -y -i %1 %input_param% -filter_complex "[1:a]afade=t=out:st=%fade_out_begin%:d=%fade_out_time% [fade];[0:a]adelay=1000|1000[delay];[delay]apad=pad_len=88200[pad];[pad][fade]amix=inputs=2:duration=first[mix]" -map "[mix]" -ac 1 -ar 44100 %3
) else (
	ffmpeg -y -i %1 %input_param% -filter_complex "%pre_str%concat=n=%loops%:v=0:a=1[cat];[cat]afade=t=out:st=%fade_out_begin%:d=%fade_out_time% [fade];[0:a]adelay=1000|1000[delay];[delay]apad=pad_len=88200[pad];[pad][fade]amix=inputs=2:duration=first[mix]" -map "[mix]" -ac 1 -ar 44100 %3
)









