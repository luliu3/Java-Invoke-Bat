:: fucntion	: fade_in fade_out audio using ffmpeg
:: author	: fwli2 
:: verion 	: 2016/08/11
:: guide 	: fade.bat in_audio out_audio

:: get adudio duration
for /f "delims=" %%i in ('"ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 %1"') do (set voice_dur=%%i)
set /a a=%voice_dur%

:: config fade time 
set /a fade_in_time=2
set /a fade_in_begin=0
set /a fade_out_time=4
set /a fade_out_begin=(%a%+1)-%fade_out_time%

echo %fade_in_time%
echo %fade_in_begin%
echo %fade_out_time%
echo %fade_out_begin%

ffmpeg -y -i %1 -af "afade=t=in:ss=%fade_in_begin%:d=%fade_in_time%,afade=t=out:st=%fade_out_begin%:d=%fade_out_time%" %2


