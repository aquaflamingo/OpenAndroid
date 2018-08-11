package com.robertsimoes.conscious.ui;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;;
import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.UserSettings;

public class OnBoardingActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOnBoarding();
        initNavigationPolicy();

        UserSettings.getInstance().getEditor(this).putBoolean(UserSettings.KEY_OPT_IN, true);
            //Opt in true by default
    }

    /**
     * Builds the navigation policy related to the Material Intro Slides
     *
     */
    private void initNavigationPolicy() {
        /* Enable/disable fullscreen */


        /* Add a navigation policy to define when users can go forward/backward */
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return true;
            }
        });

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int i, int i1) {
                finish();
            }
        });
    }

    /**
     * Adds all slides and prepares them for the Material onBoarding Intro
     */
    private void initOnBoarding() {
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);
        setButtonBackVisible(false);
        setButtonNextVisible(true);
        setButtonCtaVisible(false);


        /*
            Mind Wandering
         */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_slide_dmn)
                .description(R.string.description_slide_dmn)
                .image(R.drawable.draw_mind_wander)
                .background(R.color.color_material_black)
                .backgroundDark(R.color.primary_dark)
                .build());

        /*
            Scattered Thoughts
         */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_slide_scattered)
                .description(R.string.description_slide_scattered)
             .image(R.drawable.draw_scattered_thoughts)
                .background(R.color.color_material_black)
                .backgroundDark(R.color.primary_dark)
                .build());

        /*
            No Drafts
         */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_slide_no_drafts)
                .description(R.string.description_slide_no_drafts)
                .image(R.drawable.draw_no_drafts)
                .background(R.color.color_material_black)
                .backgroundDark(R.color.primary_dark)
                .build());


        /* Welcome without Opt In */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_slide_welcome_alt)
                .description(R.string.description_slide_welcome_alt)
                .image(R.drawable.draw_brain_meditate)
                .background(R.color.color_material_black)
                .backgroundDark(R.color.primary_dark)
                .canGoForward(false)
                .build());


        //TODO FLAG v1.0+
//        /* Welcome with Opt Int */
//        addSlide(new SimpleSlide.Builder()
//                .title(R.string.title_slide_welcome)
//                .description(R.string.description_sklide_welcome)
//                .image(R.drawable.draw_brain_meditate)
//                .background(R.color.color_material_black)
//                .backgroundDark(R.color.primary_dark)
//                        .buttonCtaLabel(R.string.button_slide_cta_label)
//                .buttonCtaClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UserSettings settings = new UserSettings(OnBoardingActivity.this);
//                        settings.getEditor().putBoolean(UserSettings.KEY_OPT_IN, false).apply();
//                        startActivity(new Intent(OnBoardingActivity.this,MainActivity.class));
//                       finish();
//                    }
//                })
//                .canGoForward(false)
//                .build());

        /* Dummy Slide */
        addSlide(new SimpleSlide.Builder()
                .title("")
                .description("")
                .background(R.color.color_material_red)
                .backgroundDark(R.color.color_dark_material_red)
                .build());
    }
}
