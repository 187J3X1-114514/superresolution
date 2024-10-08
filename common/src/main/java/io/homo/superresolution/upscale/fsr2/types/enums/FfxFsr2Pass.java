package io.homo.superresolution.upscale.fsr2.types.enums;

public enum FfxFsr2Pass {

    FFX_FSR2_PASS_DEPTH_CLIP,
    FFX_FSR2_PASS_RECONSTRUCT_PREVIOUS_DEPTH,
    FFX_FSR2_PASS_LOCK ,
    FFX_FSR2_PASS_ACCUMULATE,
    FFX_FSR2_PASS_ACCUMULATE_SHARPEN,
    FFX_FSR2_PASS_RCAS,
    FFX_FSR2_PASS_COMPUTE_LUMINANCE_PYRAMID,
    FFX_FSR2_PASS_GENERATE_REACTIVE ,
    FFX_FSR2_PASS_TCR_AUTOGENERATE,
    FFX_FSR2_PASS_COUNT
}
