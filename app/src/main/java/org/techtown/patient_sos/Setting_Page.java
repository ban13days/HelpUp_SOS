package org.techtown.patient_sos;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting_Page extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_SHAKE_SOS = "ShakeSOS";
    private static final String KEY_BIXBY_SOS = "BixbySOS";
    private static final String KEY_VOICE_REQUEST = "VoiceRequest";
    private static final String KEY_HELP_REQUEST = "HelpRequest";
    private static final String KEY_SENSITIVITY = "Sensitivity";
    private static final String KEY_TIMER = "Timer";
    private static final String KEY_MESSAGE = "helpmessage";
    private EditText helpmessage;
    private Switch shakeSwitch;
    private Switch bixbySwitch;
    private Switch voiceSwitch;
    private Switch helpSwitch;
    private SeekBar sensitivitySeekBar;
    private SeekBar timerSeekBar;
    private TextView sensitivityText;
    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);


        // 상태 표시줄과 네비게이션 바 색상을 강제로 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.BLACK); // 검정색 상태 표시줄
            window.setNavigationBarColor(Color.BLACK); // 검정색 네비게이션 바
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Switch 초기화
        shakeSwitch = findViewById(R.id.switch_shakeMd);
        bixbySwitch = findViewById(R.id.switch_BixbiMd);
        voiceSwitch = findViewById(R.id.switch_Voice);
        helpSwitch = findViewById(R.id.switch_Help);

        // SeekBar 초기화
        sensitivitySeekBar = findViewById(R.id.seekBar_sensitivity);
        timerSeekBar = findViewById(R.id.seekBar_timer);
        sensitivityText = findViewById(R.id.textView29);
        timerText = findViewById(R.id.textView37);

        // 저장된 상태 복원
        shakeSwitch.setChecked(sharedPreferences.getBoolean(KEY_SHAKE_SOS, false));
        bixbySwitch.setChecked(sharedPreferences.getBoolean(KEY_BIXBY_SOS, false));
        voiceSwitch.setChecked(sharedPreferences.getBoolean(KEY_VOICE_REQUEST, false));
        helpSwitch.setChecked(sharedPreferences.getBoolean(KEY_HELP_REQUEST, false));

        // SeekBar 상태 복원
        int savedSensitivity = sharedPreferences.getInt(KEY_SENSITIVITY, 10); // 기본값 10
        int savedTimer = sharedPreferences.getInt(KEY_TIMER, 10); // 기본값 10
        sensitivitySeekBar.setProgress(savedSensitivity);
        timerSeekBar.setProgress(savedTimer);
        sensitivityText.setText("민감도: " + savedSensitivity);
        timerText.setText("타이머: " + savedTimer);


        // Switch 상태 변경 리스너 설정
        setSwitchListener(shakeSwitch, KEY_SHAKE_SOS);
        setSwitchListener(bixbySwitch, KEY_BIXBY_SOS);
        setSwitchListener(voiceSwitch, KEY_VOICE_REQUEST);
        setSwitchListener(helpSwitch, KEY_HELP_REQUEST);

        // SeekBar 리스너 설정
        setSeekBarListener(sensitivitySeekBar, KEY_SENSITIVITY, sensitivityText, "민감도: ");
        setSeekBarListener(timerSeekBar, KEY_TIMER, timerText, "타이머: ");

        helpmessage = findViewById(R.id.et_message2);
        Button btnsave = findViewById(R.id.button);

        // 저장된 메시지 불러오기
        loadMessage();

        // 저장 버튼 클릭 리스너 설정
        btnsave.setOnClickListener(v -> saveMessage());
    }

    // 메시지를 SharedPreferences에 저장
    private void saveMessage() {
        String message = helpmessage.getText().toString().trim(); // 메시지 내용
        sharedPreferences.edit().putString(KEY_MESSAGE, message).apply(); // SharedPreferences에 저장
        Toast.makeText(this, "메시지가 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 저장된 메시지를 불러오기
    private void loadMessage() {
        String savedMessage = sharedPreferences.getString(KEY_MESSAGE, ""); // 기본값은 빈 문자열
        helpmessage.setText(savedMessage); // 메시지를 EditText에 설정
    }

    private void setSwitchListener(Switch switchView, String preferenceKey) {
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 상태 저장
            sharedPreferences.edit().putBoolean(preferenceKey, isChecked).apply();
        });
    }

    private void setSeekBarListener(SeekBar seekBar, String preferenceKey, TextView textView, String prefix) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(prefix + progress);
                sharedPreferences.edit().putInt(preferenceKey, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();

        // 각 Switch의 상태를 SharedPreferences에서 가져옴
        boolean isShakeChecked = sharedPreferences.getBoolean(KEY_SHAKE_SOS, false);
        boolean isBixbyChecked = sharedPreferences.getBoolean(KEY_BIXBY_SOS, false);
        boolean isVoiceChecked = sharedPreferences.getBoolean(KEY_VOICE_REQUEST, false);
        boolean isHelpChecked = sharedPreferences.getBoolean(KEY_HELP_REQUEST, false);
        int sensitivity = sharedPreferences.getInt(KEY_SENSITIVITY, 10);
        int timer = sharedPreferences.getInt(KEY_TIMER, 10);

        // 선택 상태를 Intent에 추가
        returnIntent.putExtra("ShakeSOS", isShakeChecked);
        returnIntent.putExtra("BixbySOS", isBixbyChecked);
        returnIntent.putExtra("VoiceRequest", isVoiceChecked);
        returnIntent.putExtra("HelpRequest", isHelpChecked);
        returnIntent.putExtra("Sensitivity", sensitivity);
        returnIntent.putExtra("Timer", timer);

        setResult(RESULT_OK, returnIntent); // 결과 전달
        super.onBackPressed(); // 기본 동작 수행
    }
}