-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers public class * extends com.markantoni.linies.ui.config.holders.BaseConfigViewHolder {
	public <init>(...);
}